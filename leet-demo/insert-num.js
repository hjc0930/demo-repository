/**
 *
 * @param {number[]} num1
 * @param {number} m
 * @param {number[]} num2
 * @param {number} n
 */
const merge = (num1, m, num2, n) => {
  let i = m - 1,
    j = n - 1,
    k = m + n - 1;

  while (i >= 0 && j >= 0) {
    if (num1[i] >= num2[j]) {
      num1[k] = num1[i];
      i--;
    } else {
      num1[k] = num2[j];
      j--;
    }
    k--;
  }
  while (j >= 0) {
    num1[k] = num2[j];
    j--;
    k--;
  }
};

/**
 * @param {number}
 */
const num1 = [1, 3, 6];
/**
 * @param {number}
 */
const num2 = [2, 4, 5];

merge(num1, 3, num2, 3);

console.log(num1);
