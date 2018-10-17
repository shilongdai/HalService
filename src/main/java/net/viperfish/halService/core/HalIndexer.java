package net.viperfish.halService.core;

import java.io.IOException;

public interface HalIndexer {

	void index(SearchEngineCrawledData data);

	void init() throws IOException;

}
