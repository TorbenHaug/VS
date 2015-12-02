package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;

import java.util.List;

public class BoardDTO {
    private List<FieldDTO> fields;

    public BoardDTO(List<FieldDTO> fields) {
        this.fields = fields;
    }

    public BoardDTO() {
    }

    public List<FieldDTO> getFields() {
        return fields;
    }
}
