package bg.geist.domain.entity.enums;

public enum Certification {
    NONE,
    ON_SERVER,
    ON_SERVER_STRICT;

    private static final Certification[] values = values();
    public static Certification byOrdinal(Integer ordinal) {
        return (ordinal != null && ordinal > -1 && ordinal < values.length) ? values[ordinal] : values[0];
    }
}