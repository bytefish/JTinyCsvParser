package de.bytefish.jtinycsvparser.mapping;


public class CsvMappingResult<TEntity> {

    private TEntity result;

    private CsvMappingError error;

    public CsvMappingResult(TEntity result) {
        this.result = result;
    }

    public CsvMappingResult(CsvMappingError error) {
        this.error = error;
    }

    public boolean isValid() {
        return error == null;
    }

    public TEntity getResult() {
        return result;
    }

    public CsvMappingError getError() {
        return error;
    }
}
