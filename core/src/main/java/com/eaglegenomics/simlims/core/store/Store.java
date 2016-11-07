package com.eaglegenomics.simlims.core.store;

import java.io.IOException;
import java.util.Collection;

/**
 * com.eaglegenomics.simlims.core.store
 * <p/>
 * Info
 *
 * @author Rob Davey
 * @since 0.0.2
 */
public interface Store<T> {
  public long save(T t) throws IOException;

  public T get(long id) throws IOException;

  public Collection<T> listAll() throws IOException;
}
