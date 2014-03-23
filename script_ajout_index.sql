ALTER TABLE reformatted_abstract_node ADD INDEX index_nodeid (superNodeId);

ALTER TABLE reformatted_pathway_relationpair ADD INDEX index_nodes (nodeA,nodeB);

ALTER TABLE reformatted_entity_particpant ADD INDEX ind_ent_partId (participantId);

ALTER TABLE reformatted_entity_particpant ADD INDEX ind_ent_entId (entityId);

ALTER TABLE reformatted_entity_information ADD INDEX ind_ent_info (entityId);

ALTER TABLE reformatted_sub_entity ADD INDEX index_sub_entity (entityId(300));