package net.viperfish.halService.core;

import java.net.URL;
import org.springframework.data.repository.CrudRepository;

public interface MapperRepository extends CrudRepository<URLChecksumMapper, URL> {

	URLChecksumMapper findByChecksum(String checksum);

}
