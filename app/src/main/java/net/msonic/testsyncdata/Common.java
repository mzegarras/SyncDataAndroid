package net.msonic.testsyncdata;

/**
 * Created by manuelzegarra on 20/02/16.
 */
public class Common {

    public enum ConflictHandling {

        TIMESTAMPPRIORITY(1),		// Conflict handling: version with most recent timestamp_lastupdate is used
        SERVERPRIORITY(2),// Conflict handling: version from server is used
        CLIENTPRIORITY(3); // Conflict handling: version from client is used




        private final int value;

        private ConflictHandling(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

}
