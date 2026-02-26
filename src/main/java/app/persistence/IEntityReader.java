package app.persistence;

public interface IEntityReader<T, I>
{
    T getByID(I id);
}
