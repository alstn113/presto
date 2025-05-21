import { useEffect, useRef, type RefObject } from 'react';

interface UseIntersectionObserverProps {
  onIntersect: () => void;
  hasNextPage?: boolean;
  threshold?: number;
  delayMs?: number;
}

const useIntersectionObserver = ({
  onIntersect,
  hasNextPage = true,
  threshold = 0.5,
  delayMs = 300,
}: UseIntersectionObserverProps): RefObject<HTMLDivElement | null> => {
  const ref = useRef<HTMLDivElement>(null);
  const timerRef = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    const target = ref.current;

    if (!target || !hasNextPage) return;

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            // debounce
            if (timerRef.current) clearTimeout(timerRef.current);
            timerRef.current = setTimeout(() => {
              onIntersect();
            }, delayMs);
          }
        });
      },
      {
        threshold,
      }
    );

    observer.observe(target);

    return () => {
      observer.disconnect();
      if (timerRef.current) clearTimeout(timerRef.current);
    };
  }, [onIntersect, hasNextPage, threshold, delayMs]);

  return ref;
};

export default useIntersectionObserver;
