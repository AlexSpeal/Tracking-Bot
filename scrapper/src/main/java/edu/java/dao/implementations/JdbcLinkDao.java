package edu.java.dao.implementations;

import edu.java.dao.interfaces.LinkRepository;
import edu.java.dto.jdbc.LinkDto;
import edu.java.errors.DuplicateLinkException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
@SuppressWarnings("MagicNumber")
public class JdbcLinkDao implements LinkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void add(LinkDto link) {
        try {
            jdbcTemplate.update("INSERT INTO link(url,updated_at,last_update,type,data) VALUES (?,?,?,?,?)",
                link.getUrl().toString(), link.getUpdatedAt(), link.getLastUpdate(), link.getType(), link.getData()
            );
        } catch (DataAccessException exception) {
            throw new DuplicateLinkException("Введена существующая ссылка!");
        }
    }

    @Transactional
    @Override
    public void remove(Long id) {
        jdbcTemplate.update("DELETE FROM link WHERE link_id=?", id);
    }

    @Transactional
    @Override
    public List<LinkDto> findAll() {
        return jdbcTemplate.query("SELECT * FROM link", new BeanPropertyRowMapper<>(LinkDto.class));
    }

    @Override
    public List<LinkDto> getByUri(URI uri) {
        return jdbcTemplate.query(
            "SELECT * FROM link WHERE url=?",
            new BeanPropertyRowMapper<>(LinkDto.class),
            uri.toString()
        );
    }

    @Override
    public void updateData(long linkId, OffsetDateTime time, String data) {
        jdbcTemplate.update("UPDATE link SET updated_at=?,data=? WHERE link_id=?", time, data, linkId);
    }

    @Override
    public void updateCheckTime(long linkId, OffsetDateTime time) {
        jdbcTemplate.update("UPDATE link SET last_update=? WHERE link_id=?", time, linkId);
    }

    @Override
    public List<LinkDto> findOldLinksToCheck(OffsetDateTime time) {
        return jdbcTemplate.query(
            "SELECT * FROM link WHERE  last_update < ?",
            new BeanPropertyRowMapper<>(LinkDto.class),
            time
        );

    }
}
