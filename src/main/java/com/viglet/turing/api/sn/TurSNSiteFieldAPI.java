package com.viglet.turing.api.sn;

import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.turing.persistence.model.sn.TurSNSite;
import com.viglet.turing.persistence.model.sn.TurSNSiteField;
import com.viglet.turing.persistence.repository.sn.TurSNSiteFieldRepository;
import com.viglet.turing.persistence.repository.sn.TurSNSiteRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/sn/{snSiteId}/field")
@Api(tags = "Semantic Navigation Field", description = "Semantic Navigation Field API")
public class TurSNSiteFieldAPI {

	@Autowired
	TurSNSiteRepository turSNSiteRepository;
	@Autowired
	TurSNSiteFieldRepository turSNSiteFieldRepository;

	@ApiOperation(value = "Semantic Navigation Site Field List")
	@GetMapping
	public List<TurSNSiteField> turSNSiteFieldList(@PathVariable String snSiteId) throws JSONException {
		TurSNSite turSNSite = turSNSiteRepository.findById(snSiteId).get();
		return this.turSNSiteFieldRepository.findByTurSNSite(turSNSite);
	}

	@ApiOperation(value = "Show a Semantic Navigation Site Field")
	@GetMapping("/{id}")
	public TurSNSiteField turSNSiteFieldGet(@PathVariable String snSiteId, @PathVariable String id) throws JSONException {
		return this.turSNSiteFieldRepository.findById(id).get();
	}

	@ApiOperation(value = "Update a Semantic Navigation Site Field")
	@PutMapping("/{id}")
	public TurSNSiteField turSNSiteFieldUpdate(@PathVariable String snSiteId, @PathVariable String id,
			@RequestBody TurSNSiteField turSNSiteField) throws Exception {
		TurSNSiteField turSNSiteFieldEdit = this.turSNSiteFieldRepository.findById(id).get();
		turSNSiteFieldEdit.setDescription(turSNSiteField.getDescription());
		turSNSiteFieldEdit.setMultiValued(turSNSiteField.getMultiValued());
		turSNSiteFieldEdit.setName(turSNSiteField.getName());
		turSNSiteFieldEdit.setType(turSNSiteField.getType());
		this.turSNSiteFieldRepository.save(turSNSiteFieldEdit);
		return turSNSiteFieldEdit;
	}

	@Transactional
	@ApiOperation(value = "Delete a Semantic Navigation Site Field")
	@DeleteMapping("/{id}")
	public boolean turSNSiteFieldDelete(@PathVariable String snSiteId, @PathVariable String id) {
		this.turSNSiteFieldRepository.delete(id);
		return true;
	}

	@ApiOperation(value = "Create a Semantic Navigation Site Field")
	@PostMapping
	public TurSNSiteField turSNSiteFieldAdd(@PathVariable String snSiteId, @RequestBody TurSNSiteField turSNSiteField) throws Exception {
		TurSNSite turSNSite = turSNSiteRepository.findById(snSiteId).get();
		turSNSiteField.setTurSNSite(turSNSite);
		this.turSNSiteFieldRepository.save(turSNSiteField);
		return turSNSiteField;

	}

}