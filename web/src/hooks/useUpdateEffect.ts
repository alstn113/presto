import {
  useEffect,
  useRef,
  type DependencyList,
  type EffectCallback,
} from 'react';

const useUpdateEffect = (effect: EffectCallback, deps?: DependencyList) => {
  const isInitialMount = useRef(true);

  useEffect(() => {
    if (isInitialMount.current) {
      isInitialMount.current = false;
      return;
    }

    return effect();
  }, deps);
};
export default useUpdateEffect;
