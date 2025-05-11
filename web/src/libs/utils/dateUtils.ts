import {
  format,
  isToday,
  isYesterday,
  differenceInMinutes,
  differenceInDays,
} from 'date-fns';
import { ko } from 'date-fns/locale';

export const formatRelativeDate = (date: string | Date): string => {
  const d = date instanceof Date ? date : new Date(date);
  const minutesDiff = differenceInMinutes(new Date(), d);

  if (minutesDiff < 3) {
    return '방금 전';
  }

  if (isToday(d)) {
    return format(d, 'a h:mm', { locale: ko }); // 오전 3:24
  }

  if (isYesterday(d)) {
    return '어제';
  }

  if (differenceInDays(new Date(), d) < 7) {
    return format(d, 'EEEE', { locale: ko }); // 월요일
  }

  return format(d, 'yyyy. MM. dd'); // 2024. 05. 07
};
