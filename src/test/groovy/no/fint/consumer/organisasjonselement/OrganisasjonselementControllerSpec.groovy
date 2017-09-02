package no.fint.consumer.organisasjonselement

import no.fint.audit.FintAuditService
import no.fint.consumer.utils.RestEndpoints
import no.fint.event.model.HeaderConstants
import no.fint.model.administrasjon.organisasjon.Organisasjonselement
import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.relation.FintResource
import no.fint.test.utils.MockMvcSpecification
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc

class OrganisasjonselementControllerSpec extends MockMvcSpecification {
    private OrganisasjonselementController controller
    private OrganisasjonselementCacheService cacheService
    private OrganisasjonselementAssembler assembler
    private MockMvc mockMvc

    void setup() {
        cacheService = Mock(OrganisasjonselementCacheService)
        assembler = Mock(OrganisasjonselementAssembler) {
            resource(_) >> ResponseEntity.ok().build()
            resources(_) >> ResponseEntity.ok().build()
        }
        controller = new OrganisasjonselementController(cacheService: cacheService, assembler: assembler, fintAuditService: Mock(FintAuditService))
        mockMvc = standaloneSetup(controller)
    }

    def "GET last updated"() {
        when:
        def response = mockMvc.perform(get("${RestEndpoints.ORGANISASJONSELEMENT}/last-updated")
                .header(HeaderConstants.ORG_ID, 'mock.no'))

        then:
        1 * cacheService.getLastUpdated(_ as String) >> 123L
        response.andExpect(status().isOk())
                .andExpect(jsonPathEquals('$.lastUpdated', '123'))
    }

    def "GET organisasjonselementer"() {
        when:
        def response = mockMvc.perform(get(RestEndpoints.ORGANISASJONSELEMENT)
                .header(HeaderConstants.ORG_ID, 'rogfk.no')
                .header(HeaderConstants.CLIENT, 'test')
        )

        then:
        1 * cacheService.getAll('rogfk.no') >> [FintResource.with(new Organisasjonselement()), FintResource.with(new Organisasjonselement())]
        response.andExpect(status().isOk())
    }

    def "GET organisasjonselement by organisasjonsId"() {
        when:
        def response = mockMvc.perform(get("${RestEndpoints.ORGANISASJONSELEMENT}/organisasjonsId/123")
                .header(HeaderConstants.ORG_ID, 'rogfk.no')
                .header(HeaderConstants.CLIENT, 'test')
        )

        then:
        1 * cacheService.getOrganisasjonselementById('rogfk.no', '123') >> [FintResource.with(new Organisasjonselement(organisasjonsId: new Identifikator(identifikatorverdi: '123')))]
        response.andExpect(status().isOk())
    }

    def "GET organisasjonselement by organisasjonsKode"() {
        when:
        def response = mockMvc.perform(get("${RestEndpoints.ORGANISASJONSELEMENT}/organisasjonsKode/abc")
                .header(HeaderConstants.ORG_ID, 'rogfk.no')
                .header(HeaderConstants.CLIENT, 'test')
        )

        then:
        1 * cacheService.getOrganisasjonselementByKode('rogfk.no', 'abc') >> [FintResource.with(new Organisasjonselement(organisasjonsKode: new Identifikator(identifikatorverdi: 'abc')))]
        response.andExpect(status().isOk())
    }
}
