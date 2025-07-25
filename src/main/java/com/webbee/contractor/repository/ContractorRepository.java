package com.webbee.contractor.repository;

import com.webbee.contractor.dto.ContractorSearchRequest;
import com.webbee.contractor.model.Contractor;
import com.webbee.contractor.row_mapper.ContractorRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Репозиторий для доступа к данным о контрагентах через NamedParameterJdbcTemplate.
 * @author Evseeva Tsvetolina
 */
@Repository
public class ContractorRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String FIND_ALL = "SELECT * FROM contractor WHERE is_active = TRUE";
    private static final String FIND_BY_ID = "SELECT * FROM contractor WHERE id = :id";
    private static final String UPDATE = """
        UPDATE contractor
        SET parent_id = :parentId,
            name = :name,
            name_full = :nameFull,
            inn = :inn,
            ogrn = :ogrn,
            country = :country,
            industry = :industry,
            org_form = :orgForm,
            modify_date = now(),
            modify_user_id = :modifyUserId,
            is_active = :isActive
        WHERE id = :id
            """;
    private static final String INSERT = """
            INSERT INTO contractor (
                        id, parent_id, name, name_full, inn, ogrn, country, industry, org_form,
                        create_date, modify_date, create_user_id, modify_user_id, is_active
                    ) VALUES (
                        :id, :parentId, :name, :nameFull, :inn, :ogrn, :country, :industry, :orgForm,
                        now(), now(), :createUserId, :modifyUserId, :isActive
                    )
            """;
    private static final String DELETE = "UPDATE contractor SET is_active = FALSE, modify_date = now() WHERE id = :id";

    public ContractorRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Возвращает список всех активных контрагентов.
     */
    public List<Contractor> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL, new ContractorRowMapper());
    }

    /**
     * Находит контрагента по Id.
     */
    public Optional<Contractor> findById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<Contractor> list = namedParameterJdbcTemplate.query(FIND_BY_ID, params, new ContractorRowMapper());
        return list.stream().findAny();
    }

    /**
     * Сохраняет нового или обновляет существующего контрагента.
     */
    public void save(Contractor contractor) {
        if (!findById(contractor.getId()).isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            params.put("parentId", contractor.getParentId());
            params.put("name", contractor.getName());
            params.put("nameFull", contractor.getNameFull());
            params.put("inn", contractor.getInn());
            params.put("ogrn", contractor.getOgrn());
            params.put("country", contractor.getCountry());
            params.put("industry", contractor.getIndustry());
            params.put("orgForm", contractor.getOrgForm());
            params.put("createDate", contractor.getCreateDate());
            params.put("modifyDate", LocalDateTime.now());
            params.put("createUserId", contractor.getCreateUserId());
            params.put("modifyUserId", contractor.getModifyUserId());
            params.put("isActive", contractor.getIsActive());
            params.put("id", contractor.getId());
            namedParameterJdbcTemplate.update(UPDATE, params);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("parentId", contractor.getParentId());
            params.put("name", contractor.getName());
            params.put("nameFull", contractor.getNameFull());
            params.put("inn", contractor.getInn());
            params.put("ogrn", contractor.getOgrn());
            params.put("country", contractor.getCountry());
            params.put("industry", contractor.getIndustry());
            params.put("orgForm", contractor.getOrgForm());
            params.put("createDate", LocalDateTime.now());
            params.put("modifyDate", LocalDateTime.now());
            params.put("createUserId", contractor.getCreateUserId());
            params.put("modifyUserId", contractor.getModifyUserId());
            params.put("isActive", contractor.getIsActive());
            params.put("id", contractor.getId());
            namedParameterJdbcTemplate.update(INSERT, params);
        }
    }

    /**
     * Логически удаляет контрагента (is_active = false).
     */
    public void delete(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        namedParameterJdbcTemplate.update(DELETE, params);
    }

    /**
     * Выполняет поиск контрагентов по фильтрам с пагинацией.
     */
    public List<Contractor> search(ContractorSearchRequest contractorSearchRequest) {
        StringBuilder sql = new StringBuilder("""
                SELECT c.* FROM contractor c
                LEFT JOIN country cn ON c.country = cn.id
                LEFT JOIN org_form of ON c.org_form = of.id
                WHERE c.is_active = TRUE
                """);
        Map<String, Object> params = new HashMap<>();

        if (contractorSearchRequest.getContractorId() != null) {
            sql.append(" AND c.id = :contractorId");
            params.put("contractorId", contractorSearchRequest.getContractorId());
        }
        if (contractorSearchRequest.getParentId() != null) {
            sql.append(" AND c.parent_id = :parentId");
            params.put("parentId", contractorSearchRequest.getParentId());
        }
        if (!contractorSearchRequest.getContractorSearch().isBlank()) {
            sql.append(" AND (LOWER(c.name) LIKE :search OR LOWER(c.name_full) LIKE :search OR LOWER(c.inn) LIKE :search OR LOWER(c.ogrn) LIKE :search)");
            params.put("search", "%" + contractorSearchRequest.getContractorSearch().toLowerCase() + "%");
        }
        if (contractorSearchRequest.getIndustry() != null) {
            sql.append(" AND c.industry = :industry");
            params.put("industry", contractorSearchRequest.getIndustry());
        }
        if (contractorSearchRequest.getOrgForm().isBlank()) {
            sql.append(" AND LOWER(of.name) LIKE :orgForm");
            params.put("orgForm", "%" + contractorSearchRequest.getOrgForm().toLowerCase() + "%");
        }

        int page = contractorSearchRequest.getPage() != null ? contractorSearchRequest.getPage() : 0;
        int size = contractorSearchRequest.getSize() != null ? contractorSearchRequest.getSize() : 20;
        sql.append(" ORDER BY c.id LIMIT :limit OFFSET :offset");
        params.put("limit", size);
        params.put("offset", page * size);

        return namedParameterJdbcTemplate.query(sql.toString(), params, new ContractorRowMapper());
    }

}
