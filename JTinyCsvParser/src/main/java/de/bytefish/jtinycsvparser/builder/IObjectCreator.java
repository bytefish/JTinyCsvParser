package de.bytefish.jtinycsvparser.builder;

@FunctionalInterface
public interface IObjectCreator<TEntity> {
    /**
     * Creates a new instance for
     *
     * @return
     */
    TEntity create();
}
