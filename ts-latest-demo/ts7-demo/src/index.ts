// TypeScript 7 Demo - 展示一些基本的 TS 特性

interface User {
  id: number;
  name: string;
  email: string;
  role: "admin" | "user" | "guest";
}

function greetUser(user: User): string {
  return `Hello, ${user.name} (${user.role})!`;
}

function createUser(
  id: number,
  name: string,
  email: string,
  role: User["role"] = "user",
): User {
  return { id, name, email, role };
}

// 泛型示例
function filterByRole<T extends User>(users: T[], role: User["role"]): T[] {
  return users.filter((user) => user.role === role);
}

// 使用示例
const users: User[] = [
  createUser(1, "Alice", "alice@example.com", "admin"),
  createUser(2, "Bob", "bob@example.com", "user"),
  createUser(3, "Charlie", "charlie@example.com", "guest"),
  createUser(4, "Diana", "diana@example.com", "admin"),
];

console.log("All users:");
users.forEach((user) => console.log("  " + greetUser(user)));

const admins = filterByRole(users, "admin");
console.log("\nAdmins:");
admins.forEach((user) => console.log(`  - ${user.name}`));
