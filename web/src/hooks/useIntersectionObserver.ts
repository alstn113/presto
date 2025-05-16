import { useEffect, useRef } from 'react';

interface useIntersectionObserverProps {
  onIntersect: () => void;
  threshold?: number;
}

const useIntersectionObserver = ({
  onIntersect,
  threshold,
}: useIntersectionObserverProps) => {
  const targetElement = useRef(null);

  useEffect(() => {
    if (!targetElement || !targetElement.current) return;

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => entry.isIntersecting && onIntersect());
      },
      {
        threshold: threshold || 0.5,
      }
    );

    observer.observe(targetElement && targetElement.current);

    return () => {
      observer.disconnect();
    };
  }, [onIntersect, threshold]);

  return targetElement;
};

export default useIntersectionObserver;
