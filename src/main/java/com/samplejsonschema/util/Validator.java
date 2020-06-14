package com.samplejsonschema.util;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.List;

public class Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Validator.class);

    public Validator() {
    }

    // esta es la version que usa everit
    public String isValid() {
        String res = "";
        String error = "";
        boolean hasError = false;
        try {
            File file = null;
            InputStream jsonst = null;
            try {
                file = ResourceUtils.getFile("classpath:schema.json");
                File filejson = ResourceUtils.getFile("classpath:file.json");
                jsonst = new DataInputStream(new FileInputStream(filejson));
            } catch (FileNotFoundException e) {
                hasError = true;
                error += e.getMessage();
            }
            try (InputStream inputStream = new DataInputStream(new FileInputStream(file))) {
                JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
                Schema schema = SchemaLoader.load(rawSchema);
                schema.validate(new JSONObject(new JSONTokener(jsonst)));
            } catch (IOException e) {
                hasError = true;
                error += e.getMessage();
            }
        } catch (ValidationException e) {
            hasError = true;
            error += e.getMessage();
            String error2 = "";
            List<ValidationException> causes = (List<ValidationException>) e.getCausingExceptions();
            int causesSize = causes.size();
            for (int i = 0; i < causesSize; i++) {
                error2 += " - error " + (i + 1) + " : " + causes.get(i).getMessage() + "<br>\n";
            }
            error += "<br><br>\n\n - se han producido " + causesSize + " errores : <br><br>\n\n" + error2;
        } catch (Exception e) {
            hasError = true;
            error += e.getMessage();
        }

        if (hasError) {
            res = error;
        } else {
            res = " - json correctamente validado usando el schema";
        }
        return res;
    }

}
