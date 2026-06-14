package builder;

public class User {

    // Required fields
    private final String name;
    private final String email;
    private final String phone;

    // Optional fields
    private final String address;
    private final String city;
    private final String pincode;
    private final String profilePicUrl;
    private final boolean isVeg;
    private final String preferredCuisine;
    private final String referralCode;
    private final int loyaltyPoints;

    private User(Builder builder) {
        this.name             = builder.name;
        this.email            = builder.email;
        this.phone            = builder.phone;
        this.address          = builder.address;
        this.city             = builder.city;
        this.pincode          = builder.pincode;
        this.profilePicUrl    = builder.profilePicUrl;
        this.isVeg            = builder.isVeg;
        this.preferredCuisine = builder.preferredCuisine;
        this.referralCode     = builder.referralCode;
        this.loyaltyPoints    = builder.loyaltyPoints;
    }

    public String getName()             { return name; }
    public String getEmail()            { return email; }
    public String getPhone()            { return phone; }
    public String getAddress()          { return address; }
    public String getCity()             { return city; }
    public String getPincode()          { return pincode; }
    public String getProfilePicUrl()    { return profilePicUrl; }
    public boolean isVeg()              { return isVeg; }
    public String getPreferredCuisine() { return preferredCuisine; }
    public String getReferralCode()     { return referralCode; }
    public int getLoyaltyPoints()       { return loyaltyPoints; }

    @Override
    public String toString() {
        return "User {" +
                "\n  name             = " + name +
                "\n  email            = " + email +
                "\n  phone            = " + phone +
                "\n  address          = " + (address != null ? address : "N/A") +
                "\n  city             = " + (city != null ? city : "N/A") +
                "\n  pincode          = " + (pincode != null ? pincode : "N/A") +
                "\n  profilePicUrl    = " + (profilePicUrl != null ? profilePicUrl : "N/A") +
                "\n  isVeg            = " + isVeg +
                "\n  preferredCuisine = " + (preferredCuisine != null ? preferredCuisine : "N/A") +
                "\n  referralCode     = " + (referralCode != null ? referralCode : "N/A") +
                "\n  loyaltyPoints    = " + loyaltyPoints +
                "\n}";
    }

    // ----------------------------------------------------------------
    // Builder
    // ----------------------------------------------------------------
    public static class Builder {

        // Required
        private final String name;
        private final String email;
        private final String phone;

        // Optional — defaults
        private String address          = null;
        private String city             = null;
        private String pincode          = null;
        private String profilePicUrl    = null;
        private boolean isVeg           = false;
        private String preferredCuisine = null;
        private String referralCode     = null;
        private int loyaltyPoints       = 0;

        public Builder(String name, String email, String phone) {
            if (name == null || name.isBlank())   throw new IllegalArgumentException("Name is required");
            if (email == null || email.isBlank())  throw new IllegalArgumentException("Email is required");
            if (phone == null || phone.isBlank())  throw new IllegalArgumentException("Phone is required");
            this.name  = name;
            this.email = email;
            this.phone = phone;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder pincode(String pincode) {
            this.pincode = pincode;
            return this;
        }

        public Builder profilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
            return this;
        }

        public Builder isVeg(boolean isVeg) {
            this.isVeg = isVeg;
            return this;
        }

        public Builder preferredCuisine(String preferredCuisine) {
            this.preferredCuisine = preferredCuisine;
            return this;
        }

        public Builder referralCode(String referralCode) {
            this.referralCode = referralCode;
            return this;
        }

        public Builder loyaltyPoints(int loyaltyPoints) {
            this.loyaltyPoints = loyaltyPoints;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}