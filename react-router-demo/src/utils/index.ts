import { bignumber } from "mathjs";

export const calculatePercent = (
  oldVal: number | string,
  newVal: number | string
): string => {
  const oldValInstance = bignumber(oldVal.toString());
  const newValInstance = bignumber(newVal.toString());

  if (oldValInstance.comparedTo(newValInstance) === 0) {
    return "0";
  }

  const diff = newValInstance.sub(oldValInstance);
  const rate = diff.dividedBy(oldValInstance);
  const symbol = newValInstance.comparedTo(oldValInstance) > 0 ? "+" : "";

  return `${symbol}${rate.toString()}%`;
};
