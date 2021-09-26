package com.mytiki.kgraph.features.latest.hibp;

import java.util.EnumSet;

public enum HibpWeight {
    /** BREACH TYPES **/
    TYPE_IS_RETIRED(-0.75, "isFabricated"),
    TYPE_IS_FABRICATED(-0.5, "isFabricated"),
    TYPE_IS_SPAM_LIST(0.25, "isSpamList"),
    TYPE_IS_VERIFIED(0.55, "isVerified"),
    TYPE_IS_SENSITIVE(0.75, "isSensitive"),

    /** BREACH DATA CLASSES **/
    CLASS_ACCOUNT_BALANCES(8.0,"Account balances"),
    CLASS_ADDRESS_BOOK_CONTACTS(7.0,"Address book contacts"),
    CLASS_AGE_GROUPS(2.0,"Age groups"),
    CLASS_AGES(4.0,"Ages"),
    CLASS_APPS_INSTALLED_ON_DEVICES(3.0,"Apps installed on devices"),
    CLASS_ASTROLOGICAL_SIGNS(2.0,"Astrological signs"),
    CLASS_AUDIO_RECORDINGS(9.0,"Audio recordings"),
    CLASS_AUTH_TOKENS(9.0,"Auth tokens"),
    CLASS_AVATARS(6.0,"Avatars"),
    CLASS_BANK_ACCOUNT_NUMBERS(10.0,"Bank account numbers"),
    CLASS_BEAUTY_RATINGS(3.0,"Beauty ratings"),
    CLASS_BIOMETRIC_DATA(10.0,"Biometric data"),
    CLASS_BIOS(3.0,"Bios"),
    CLASS_BROWSER_USER_AGENT_DETAILS(1.0,"Browser user agent details"),
    CLASS_BROWSING_HISTORIES(8.0,"Browsing histories"),
    CLASS_BUYING_PREFERENCES(2.0,"Buying preferences"),
    CLASS_CAR_OWNERSHIP_STATUSES(3.0,"Car ownership statuses"),
    CLASS_CAREER_LEVELS(3.0,"Career levels"),
    CLASS_CELLULAR_NETWORK_NAMES(1.0,"Cellular network names"),
    CLASS_CHARITABLE_DONATIONS(3.0,"Charitable donations"),
    CLASS_CHAT_LOGS(8.0,"Chat logs"),
    CLASS_CREDIT_CARD_CVV(10.0,"Credit card CVV"),
    CLASS_CREDIT_CARDS(10.0,"Credit cards"),
    CLASS_CREDIT_STATUS_INFORMATION(5.0,"Credit status information"),
    CLASS_CUSTOMER_FEEDBACK(3.0,"Customer feedback"),
    CLASS_CUSTOMER_INTERACTIONS(2.0,"Customer interactions"),
    CLASS_DATES_OF_BIRTH(8.0,"Dates of birth"),
    CLASS_DECEASED_DATE(5.0,"Deceased date"),
    CLASS_DECEASED_STATUSES(3.0,"Deceased statuses"),
    CLASS_DEVICE_INFORMATION(1.0,"Device information"),
    CLASS_DEVICE_SERIAL_NUMBERS(2.0,"Device serial numbers"),
    CLASS_DEVICE_USAGE_TRACKING_DATA(3.0,"Device usage tracking data"),
    CLASS_DRINKING_HABITS(6.0,"Drinking habits"),
    CLASS_DRUG_HABITS(9.0,"Drug habits"),
    CLASS_EATING_HABITS(4.0,"Eating habits"),
    CLASS_EDUCATION_LEVELS(3.0,"Education levels"),
    CLASS_EMAIL_ADDRESSES(7.0,"Email addresses"),
    CLASS_EMAIL_MESSAGES(8.0,"Email messages"),
    CLASS_EMPLOYERS(2.0,"Employers"),
    CLASS_EMPLOYMENT_STATUSES(3.0,"Employment statuses"),
    CLASS_ENCRYPTED_KEYS(4.0,"Encrypted keys"),
    CLASS_ETHNICITIES(4.0,"Ethnicities"),
    CLASS_FAMILY_MEMBERS_NAMES(6.0,"Family members\' names"),
    CLASS_FAMILY_PLANS(5.0,"Family plans"),
    CLASS_FAMILY_STRUCTURE(3.0,"Family structure"),
    CLASS_FINANCIAL_INVESTMENTS(7.0,"Financial investments"),
    CLASS_FINANCIAL_TRANSACTIONS(9.0,"Financial transactions"),
    CLASS_FITNESS_LEVELS(3.0,"Fitness levels"),
    CLASS_GENDERS(1.0,"Genders"),
    CLASS_GEOGRAPHIC_LOCATIONS(7.0,"Geographic locations"),
    CLASS_GOVERNMENT_ISSUED_IDS(10.0,"Government issued IDs"),
    CLASS_HEALTH_INSURANCE_INFORMATION(9.0,"Health insurance information"),
    CLASS_HISTORICAL_PASSWORDS(10.0,"Historical passwords"),
    CLASS_HOME_LOAN_INFORMATION(3.0,"Home loan information"),
    CLASS_HOME_OWNERSHIP_STATUSES(3.0,"Home ownership statuses"),
    CLASS_HOMEPAGE_URLS(1.0,"Homepage URLs"),
    CLASS_IMEI_NUMBERS(2.0,"IMEI numbers"),
    CLASS_IMSI_NUMBERS(2.0,"IMSI numbers"),
    CLASS_INCOME_LEVELS(3.0,"Income levels"),
    CLASS_INSTANT_MESSENGER_IDENTITIES(4.0,"Instant messenger identities"),
    CLASS_IP_ADDRESSES(3.0,"IP addresses"),
    CLASS_JOB_APPLICATIONS(7.0,"Job applications"),
    CLASS_JOB_TITLES(3.0,"Job titles"),
    CLASS_LICENCE_PLATES(2.0,"Licence plates"),
    CLASS_LIVING_COSTS(4.0,"Living costs"),
    CLASS_LOGIN_HISTORIES(4.0,"Login histories"),
    CLASS_MAC_ADDRESSES(6.0,"MAC addresses"),
    CLASS_MARITAL_STATUSES(1.0,"Marital statuses"),
    CLASS_MNEMONIC_PHRASES(10.0,"Mnemonic phrases"),
    CLASS_MOTHERS_MAIDEN_NAMES(5.0,"Mothers maiden names"),
    CLASS_NAMES(3.0,"Names"),
    CLASS_NATIONALITIES(2.0,"Nationalities"),
    CLASS_NET_WORTHS(6.0,"Net worths"),
    CLASS_NICKNAMES(2.0,"Nicknames"),
    CLASS_OCCUPATIONS(3.0,"Occupations"),
    CLASS_PARENTING_PLANS(4.0,"Parenting plans"),
    CLASS_PARTIAL_CREDIT_CARD_DATA(5.0,"Partial credit card data"),
    CLASS_PARTIAL_DATES_OF_BIRTH(4.0,"Partial dates of birth"),
    CLASS_PASSPORT_NUMBERS(10.0,"Passport numbers"),
    CLASS_PASSWORD_HINTS(6.0,"Password hints"),
    CLASS_PASSWORD_STRENGTHS(4.0,"Password strengths"),
    CLASS_PASSWORDS(10.0,"Passwords"),
    CLASS_PAYMENT_HISTORIES(7.0,"Payment histories"),
    CLASS_PAYMENT_METHODS(6.0,"Payment methods"),
    CLASS_PERSONAL_DESCRIPTIONS(3.0,"Personal descriptions"),
    CLASS_PERSONAL_HEALTH_DATA(9.0,"Personal health data"),
    CLASS_PERSONAL_INTERESTS(3.0,"Personal interests"),
    CLASS_PHONE_NUMBERS(5.0,"Phone numbers"),
    CLASS_PHOTOS(8.0,"Photos"),
    CLASS_PHYSICAL_ADDRESSES(8.0,"Physical addresses"),
    CLASS_PHYSICAL_ATTRIBUTES(2.0,"Physical attributes"),
    CLASS_PINS(10.0,"PINs"),
    CLASS_PLACES_OF_BIRTH(3.0,"Places of birth"),
    CLASS_POLITICAL_DONATIONS(3.0,"Political donations"),
    CLASS_POLITICAL_VIEWS(2.0,"Political views"),
    CLASS_PRIVATE_MESSAGES(7.0,"Private messages"),
    CLASS_PROFESSIONAL_SKILLS(2.0,"Professional skills"),
    CLASS_PROFILE_PHOTOS(6.0,"Profile photos"),
    CLASS_PURCHASES(7.0,"Purchases"),
    CLASS_PURCHASING_HABITS(6.0,"Purchasing habits"),
    CLASS_RACES(2.0,"Races"),
    CLASS_RECOVERY_EMAIL_ADDRESSES(5.0,"Recovery email addresses"),
    CLASS_RELATIONSHIP_STATUSES(2.0,"Relationship statuses"),
    CLASS_RELIGIONS(2.0,"Religions"),
    CLASS_REWARD_PROGRAM_BALANCES(5.0,"Reward program balances"),
    CLASS_SALUTATIONS(1.0,"Salutations"),
    CLASS_SCHOOL_GRADES(1.0,"School grades (class levels)"),
    CLASS_SECURITY_QUESTIONS_AND_ANSWERS(10.0,"Security questions and answers"),
    CLASS_SEXUAL_FETISHES(9.0,"Sexual fetishes"),
    CLASS_SEXUAL_ORIENTATIONS(5.0,"Sexual orientations"),
    CLASS_SMOKING_HABITS(8.0,"Smoking habits"),
    CLASS_SMS_MESSAGES(8.0,"SMS messages"),
    CLASS_SOCIAL_CONNECTIONS(6.0,"Social connections"),
    CLASS_SOCIAL_MEDIA_PROFILES(4.0,"Social media profiles"),
    CLASS_SOCIAL_SECURITY_NUMBERS(10.0,"Social security numbers"),
    CLASS_SPOKEN_LANGUAGES(1.0,"Spoken languages"),
    CLASS_SPOUSES_NAMES(3.0,"Spouses names"),
    CLASS_SUPPORT_TICKETS(5.0,"Support tickets"),
    CLASS_SURVEY_RESULTS(2.0,"Survey results"),
    CLASS_TAXATION_RECORDS(8.0,"Taxation records"),
    CLASS_TIME_ZONES(1.0,"Time zones"),
    CLASS_TRAVEL_HABITS(2.0,"Travel habits"),
    CLASS_USER_STATUSES(2.0,"User statuses"),
    CLASS_USER_WEBSITE_URLS(1.0,"User website URLs"),
    CLASS_USERNAMES(7.0,"Usernames"),
    CLASS_UTILITY_BILLS(8.0,"Utility bills"),
    CLASS_VEHICLE_DETAILS(5.0,"Vehicle details"),
    CLASS_WEBSITE_ACTIVITY(3.0,"Website activity"),
    CLASS_WORK_HABITS(2.0,"Work habits"),
    CLASS_YEARS_OF_PROFESSIONAL_EXPERIENCE(2.0,"Years of professional experience"),

    /** BREACH PWN COUNT **/
    COUNT_1M(0.25, 1000000),
    COUNT_10M(0.50,10000000),
    COUNT_25M(0.75,25000000),
    COUNT_100M(1.00,100000000);

    private static final EnumSet<HibpWeight> allOf = EnumSet.allOf(HibpWeight.class);

    private final double weight;
    private final Object lookup;

    HibpWeight(double weight, Object lookup) {
        this.weight = weight;
        this.lookup = lookup;
    }

    public double getWeight() {
        return weight;
    }

    public Object getLookup() {
        return lookup;
    }

    public static HibpWeight forLookup(Object lookup) {
        for (HibpWeight e : allOf) {
            if (e.getLookup().equals(lookup))
                return e;
        }
        return null;
    }
}
