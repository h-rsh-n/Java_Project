    package com.example.demo.Models;

    import org.springframework.data.annotation.Id;

    public class Candidate {
        @Id
        private String id;
        private String name;
        private String partyAffiliation;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPartyAffiliation() {
            return partyAffiliation;
        }

        public void setPartyAffiliation(String partyAffiliation) {
            this.partyAffiliation = partyAffiliation;
        }
    }
