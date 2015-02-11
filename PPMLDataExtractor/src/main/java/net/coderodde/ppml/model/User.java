package net.coderodde.ppml.model;

public class User {

    public static enum Gender {
        FEMALE,
        MALE,
        UNKNOWN;
        
        public String getTextRepresentation() {
            switch (this) {
                case FEMALE:
                    return "female";
                    
                case MALE:
                    return "male";
                    
                case UNKNOWN:
                    return "unknown gender";
                    
                default:
                    throw new IllegalStateException("Unknown gender.");
            }
        }
    };
    
    private final int userId;
    private final int age;
    private final Gender gender;
    private final String occupation;
    private final String zipCode;
    
    public User(final int userId,
                final int age,
                final Gender gender,
                final String occupation,
                final String zipCode) {
        this.userId = userId;
        this.age = age;
        this.gender = gender;
        this.occupation = occupation;
        this.zipCode = zipCode;
    }
    
    public int getUserID() {
        return userId;
    }
    
    public int getAge() {
        return age;
    }
    
    public Gender getGender() {
        return gender;
    }
    
    public String getOccupation() {
        return occupation;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(userId);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof User)) {
            return false;
        }
        
        return ((User) o).userId == this.userId;
    }
    
    @Override
    public String toString() {
        return new StringBuilder("[userId: ")
                   .append(userId)
                   .append(", age: ")
                   .append(age)
                   .append(", gender: ")
                   .append(gender.getTextRepresentation())
                   .append(", occupation: ")
                   .append(occupation)
                   .append(", ZIP code: ")
                   .append(zipCode)
                   .append("]")
                   .toString();
    }
}
