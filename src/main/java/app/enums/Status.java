package app.enums;

public enum Status
{
    RELEASED("Released"),
    IN_PRODUCTION("In Production");

    private final String value;

    Status(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public static Status fromValue(String value)
    {
        for (Status s : values())
        {
            if (s.value.equals(value))
            {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown Status value: " + value);
    }
}
