type Timer = ReturnType<typeof setTimeout>;
type CacheKey = string | number;

export interface CachedData<TData = any, TParams = any> {
  data: TData;
  params: TParams;
  time: number;
}

interface RecordData extends CachedData {
  timer: Timer | undefined;
}

const cache = new Map<CacheKey, RecordData>();

const setCache = (key: CacheKey, cacheTime: number, cacheData: CachedData) => {
  const currentCache = cache.get(key);

  if (currentCache?.timer) {
    clearTimeout(currentCache.timer);
  }

  let timer: Timer | undefined = undefined;

  if (cacheTime > -1) {
    // if cache out, clear it
    timer = setTimeout(() => {
      cache.delete(key);
    }, cacheTime);
  }

  cache.set(key, {
    ...cacheData,
    timer,
  });
};

const getCache = (key: CacheKey) => {
  return cache.get(key);
};

const clearCache = (key?: string | string[]) => {
  if (key) {
    const cacheKeys = Array.isArray(key) ? key : [key];
    cacheKeys.forEach((cacheKey) => cache.delete(cacheKey));
  } else {
    cache.clear();
  }
};

export { getCache, setCache, clearCache };
