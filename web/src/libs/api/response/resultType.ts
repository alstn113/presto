export const ResultType = {
  SUCCESS: 'SUCCESS',
  ERROR: 'ERROR',
} as const;

export type ResultType = (typeof ResultType)[keyof typeof ResultType];
