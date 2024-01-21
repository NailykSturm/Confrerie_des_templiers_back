package com.usmb.m2.confrerie_des_templier.DTO;

import java.util.HashMap;
import java.util.List;

public record GraphDTO(List<HashMap<String, Object>> nodes, List<EdgeDTO> edges) {
}
