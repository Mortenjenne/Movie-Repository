package app.enums;

public enum Role
{
    ACTOR("Acting"),
    DIRECTOR("Directing");

    private final String value;

    Role(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public static Role fromValue(String value)
    {
        for (Role r : values())
        {
            if (r.value.equals(value))
            {
                return r;
            }
        }
        throw new IllegalArgumentException("Unknown Role value: " + value);
    }
}
