package br.com.confidencecambio.javabasico.core.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class Persona {
    @NotBlank
    private String name;

    public String getFirstName() {
        return name.split(" ")[0].trim();
    }

    public void setName(String name) {
        this.name = name.replaceAll("\\s+", " ").trim();
    }

    public String getLastName() {
        return name.split(" ", 2)[1].trim();
    }

    public String getShortName() {
        var nameSplit = name.split(" ");
        StringBuilder sb = new StringBuilder(nameSplit[0].trim());
        for (int i = 1; i < nameSplit.length - 1; i++) {
            sb.append(" ").append(nameSplit[i].charAt(0)).append(".");
        }
        sb.append(" ").append(nameSplit[nameSplit.length - 1].trim());
        return sb.toString();
    }

    public String getUpperCaseName() {
        return name.toUpperCase();
    }

}
