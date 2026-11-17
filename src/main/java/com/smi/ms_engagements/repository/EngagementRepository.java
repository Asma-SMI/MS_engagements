package com.smi.ms_engagements.repository;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class EngagementRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String sqlCdi;
    private final String sqlCde;

    public EngagementRepository(
            NamedParameterJdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;

        this.sqlCdi = loadSql("sql/eng_cdi.sql");
        this.sqlCde = loadSql("sql/eng_cde.sql");
    }

    public List<Map<String, Object>> extractCdi(
            LocalDate dateArrete) {

        MapSqlParameterSource params =
                new MapSqlParameterSource()
                        .addValue(
                                "dateArrete",
                                Date.valueOf(dateArrete)
                        );

        return jdbcTemplate.queryForList(sqlCdi, params);
    }

    public List<Map<String, Object>> extractCde(
            LocalDate dateArrete) {

        MapSqlParameterSource params =
                new MapSqlParameterSource()
                        .addValue(
                                "dateArrete",
                                Date.valueOf(dateArrete)
                        );

        return jdbcTemplate.queryForList(sqlCde, params);
    }

    private String loadSql(String path) {

        try (InputStream input =
                     new ClassPathResource(path).getInputStream()) {

            return new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8
            );

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Impossible de charger le fichier SQL : " + path,
                    e
            );
        }
    }
}
