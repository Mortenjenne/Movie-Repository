package app.enums;

public enum Gender
{
    UNKNOWN(0),
    FEMALE(1),
    MALE(2),
    NON_BINARY_OTHER(3);

    private final int value;

    Gender(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static Gender fromValue(int value)
    {
        for (Gender g : values())
        {
            if (g.value == value)
            {
                return g;
            }
        }
        throw new IllegalArgumentException("Unknown Gender value: " + value);
    }
}
