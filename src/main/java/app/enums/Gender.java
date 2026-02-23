package app.enums;

public enum Gender
{
    FEMALE(1),
    MALE(2);

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
