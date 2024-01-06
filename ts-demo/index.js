/**
 * @param {string[]} strs
 * @returns {string[][]}
 */
function groupAnagrams(strs) {
  if (strs.length === 0) return [];
  if (strs.length === 1) return [strs];

  const len = strs.length;
  /**
   * @type {Map<string, string[]>}
   */
  const hash = new Map();

  for (let i = 0; i < len; i++) {
    const str = strs[i];
    const sorted = Array.from(str).sort().toString();

    if (hash.has(sorted)) {
      hash.get(sorted).push(str);
    } else {
      hash.set(sorted, [str]);
    }
  }

  return Array.from(hash.values());
}

console.log(groupAnagrams(["eat", "tea", "tan", "ate", "nat", "bat"]));
