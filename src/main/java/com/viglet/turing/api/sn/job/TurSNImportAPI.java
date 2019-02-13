package com.viglet.turing.api.sn.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.turing.persistence.model.sn.TurSNSite;
import com.viglet.turing.persistence.repository.sn.TurSNSiteRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/sn/{siteName}/import")
@Api(tags = "Semantic Navigation Import", description = "Semantic Navigation Import API")
public class TurSNImportAPI {
	static final Logger logger = LogManager.getLogger(TurSNImportAPI.class.getName());
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	@Autowired
	private TurSNSiteRepository turSNSiteRepository;

	public static final String INDEXING_QUEUE = "indexing.queue";

	@PostMapping
	public boolean turSNImportBroker(@PathVariable String siteName, @RequestBody TurSNJobItems turSNJobItems)
			throws JSONException {
		TurSNSite turSNSite = turSNSiteRepository.findByName(siteName);
		if (turSNSite != null) {
			TurSNJob turSNJob = new TurSNJob();
			turSNJob.setSiteId(turSNSite.getId());
			turSNJob.setTurSNJobItems(turSNJobItems);
			send(turSNJob);
			return true;
		} else {
			return false;
		}

	}

	public void send(TurSNJob turSNJob) {
		logger.debug("Sent job - " + INDEXING_QUEUE);
		this.jmsMessagingTemplate.convertAndSend(INDEXING_QUEUE, turSNJob);

	}
}
