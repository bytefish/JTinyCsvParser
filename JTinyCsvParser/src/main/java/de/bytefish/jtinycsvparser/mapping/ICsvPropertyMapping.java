package de.bytefish.jtinycsvparser.mapping;

public interface ICsvPropertyMapping<TEntity>
{
    boolean tryMapValue(TEntity entity, String value);
}
