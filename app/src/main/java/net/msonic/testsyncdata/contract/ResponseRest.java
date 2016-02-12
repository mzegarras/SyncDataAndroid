package net.msonic.testsyncdata.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by manuelzegarra on 12/02/16.
 */
public class ResponseRest<T> {



        @JsonProperty(value="sts")
        public int status;

        @JsonProperty(value="des")
        public String description;

        @JsonProperty(value="res")
        public T response;

}
