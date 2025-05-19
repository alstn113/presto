import { useEffect, useRef, type RefObject } from 'react';

interface useIntersectionObserverProps {
  onIntersect: () => void;
  threshold?: number;
}

const useIntersectionObserver = ({
  onIntersect,
  threshold,
}: useIntersectionObserverProps): RefObject<HTMLDivElement | null> => {
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const currentElement = ref.current;

    if (!currentElement) return;

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            onIntersect();
          }
        });
      },
      {
        threshold: threshold || 0.5,
      }
    );

    observer.observe(currentElement);

    return () => {
      observer.disconnect();
    };
  }, [onIntersect, threshold]);

  return ref;
};

export default useIntersectionObserver;
