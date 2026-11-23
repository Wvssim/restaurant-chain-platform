package com.restochain.commandesservice.service;

import com.restochain.commandesservice.client.MenuClient;
import com.restochain.commandesservice.client.SuccursaleClient;
import com.restochain.commandesservice.client.dto.PlatClientDto;
import com.restochain.commandesservice.client.dto.ReservationStockRequest;
import com.restochain.commandesservice.client.dto.SuccursaleClientDto;
import com.restochain.commandesservice.dto.CreateCommandeDto;
import com.restochain.commandesservice.entite.Commande;
import com.restochain.commandesservice.event.CommandeEventPublisher;
import com.restochain.commandesservice.exception.DonneesInvalidesException;
import com.restochain.commandesservice.exception.RessourceIntrouvableException;
import com.restochain.commandesservice.exception.ServiceIndisponibleException;
import com.restochain.commandesservice.exception.StockInsuffisantException;
import com.restochain.commandesservice.exception.SuccursaleFermeeException;
import com.restochain.commandesservice.repository.CommandeRepository;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CommandeServiceTest {

    @Mock
    private CommandeRepository commandeRepository;
    @Mock
    private SuccursaleClient succursaleClient;
    @Mock
    private MenuClient menuClient;
    @Mock
    private CommandeEventPublisher commandeEventPublisher;

    private CommandeService commandeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commandeService = new CommandeService(commandeRepository, new TransitionStatutValidator(), succursaleClient, menuClient, commandeEventPublisher);
        when(commandeRepository.save(any(Commande.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private static FeignException feignError(int status) {
        Request request = Request.create(Request.HttpMethod.GET, "/test", Collections.emptyMap(), null, new RequestTemplate());
        Response response = Response.builder().status(status).reason("erreur").request(request).build();
        return FeignException.errorStatus("Client#methode()", response);
    }

    private static SuccursaleClientDto succursaleOuverte() {
        SuccursaleClientDto dto = new SuccursaleClientDto();
        dto.setId(1L);
        dto.setNom("Le Gourmet Centre");
        dto.setVille("Casablanca");
        dto.setStatut("OUVERTE");
        return dto;
    }

    private static PlatClientDto plat(Long id, Long succursaleId, String prix, int qteDisponible) {
        PlatClientDto plat = new PlatClientDto();
        plat.setId(id);
        plat.setNom("Tagine de poulet");
        plat.setSuccursaleId(succursaleId);
        plat.setPrix(new BigDecimal(prix));
        plat.setDisponible(true);
        plat.setQteDisponible(qteDisponible);
        return plat;
    }

    private static CreateCommandeDto dtoCreation(Long succursaleId, Long platId, int quantite) {
        CreateCommandeDto dto = new CreateCommandeDto();
        dto.setSuccursaleId(succursaleId);
        dto.setNumeroTable(5);
        CreateCommandeDto.LigneCommandeCreationDto ligne = new CreateCommandeDto.LigneCommandeCreationDto();
        ligne.setPlatId(platId);
        ligne.setQuantite(quantite);
        dto.setLignes(List.of(ligne));
        return dto;
    }

    @Test
    void creation_nominale_calcule_le_prix_total() {
        when(commandeRepository.findByIdempotencyKey("k1")).thenReturn(Optional.empty());
        when(succursaleClient.getSuccursaleById(1L)).thenReturn(succursaleOuverte());
        when(menuClient.getPlatById(10L)).thenReturn(plat(10L, 1L, "95.00", 5));

        Commande commande = commandeService.createCommande(dtoCreation(1L, 10L, 2), "k1");

        assertEquals(new BigDecimal("190.00"), commande.getPrixTotal());
        verify(menuClient).reserverStock(eq(10L), any(ReservationStockRequest.class));
        verify(commandeEventPublisher).publierCreation(commande);
    }

    @Test
    void idempotence_ne_recree_pas_une_commande_existante() {
        Commande existante = new Commande();
        existante.setIdempotencyKey("k1");
        when(commandeRepository.findByIdempotencyKey("k1")).thenReturn(Optional.of(existante));

        Commande resultat = commandeService.createCommande(dtoCreation(1L, 10L, 2), "k1");

        assertSame(existante, resultat);
        verifyNoInteractions(succursaleClient, menuClient);
    }

    @Test
    void succursale_introuvable_leve_ressource_introuvable() {
        when(commandeRepository.findByIdempotencyKey(anyString())).thenReturn(Optional.empty());
        when(succursaleClient.getSuccursaleById(99L)).thenThrow(feignError(404));

        assertThrows(RessourceIntrouvableException.class,
                () -> commandeService.createCommande(dtoCreation(99L, 10L, 1), "k2"));
    }

    @Test
    void succursale_fermee_refuse_la_commande() {
        SuccursaleClientDto fermee = succursaleOuverte();
        fermee.setStatut("FERMEE");
        when(commandeRepository.findByIdempotencyKey(anyString())).thenReturn(Optional.empty());
        when(succursaleClient.getSuccursaleById(1L)).thenReturn(fermee);

        assertThrows(SuccursaleFermeeException.class,
                () -> commandeService.createCommande(dtoCreation(1L, 10L, 1), "k3"));
    }

    @Test
    void plat_d_une_autre_succursale_est_refuse() {
        when(commandeRepository.findByIdempotencyKey(anyString())).thenReturn(Optional.empty());
        when(succursaleClient.getSuccursaleById(1L)).thenReturn(succursaleOuverte());
        when(menuClient.getPlatById(10L)).thenReturn(plat(10L, 2L, "95.00", 5));

        assertThrows(DonneesInvalidesException.class,
                () -> commandeService.createCommande(dtoCreation(1L, 10L, 1), "k4"));
    }

    @Test
    void stock_insuffisant_est_traduit_localement() {
        when(commandeRepository.findByIdempotencyKey(anyString())).thenReturn(Optional.empty());
        when(succursaleClient.getSuccursaleById(1L)).thenReturn(succursaleOuverte());
        when(menuClient.getPlatById(10L)).thenReturn(plat(10L, 1L, "95.00", 1));
        when(menuClient.reserverStock(eq(10L), any(ReservationStockRequest.class))).thenThrow(feignError(409));

        assertThrows(StockInsuffisantException.class,
                () -> commandeService.createCommande(dtoCreation(1L, 10L, 5), "k5"));
    }

    @Test
    void service_distant_injoignable_devient_service_indisponible() {
        when(commandeRepository.findByIdempotencyKey(anyString())).thenReturn(Optional.empty());
        when(succursaleClient.getSuccursaleById(1L)).thenThrow(feignError(503));

        assertThrows(ServiceIndisponibleException.class,
                () -> commandeService.createCommande(dtoCreation(1L, 10L, 1), "k6"));
    }
}
