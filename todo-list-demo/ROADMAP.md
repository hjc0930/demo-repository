# Todo List Demo — Development Roadmap

A progressive development plan to take this project from scaffold to production-ready.

## Current Status

```
Backend:  Complete — Full CRUD REST API with Gin + GORM
Frontend: Scaffolded — Build tooling ready, no application code yet
```

---

## Phase 1: Connect Frontend to Backend

> Goal: Make the app functional. Understand how frontend and backend collaborate.

### 1.1 Define Types (`types/`)

Align TypeScript type definitions with the backend response structure:

```typescript
interface Todo {
  id: number;
  title: string;
  description: string;
  status: 0 | 1 | 2;   // pending / in-progress / done
  priority: 0 | 1 | 2;  // low / medium / high
  dueDate: string | null;
  createdAt: string;
  updatedAt: string;
}
```

### 1.2 API Service Layer (`services/`)

Wrap axios to communicate with the backend:

```typescript
// Unified request/response types, centralized error handling
export const todoApi = {
  list: (params) => http.get('/todos', { params }),
  create: (data) => http.post('/todos', data),
  // ...
};
```

### 1.3 Core Pages (`views/`)

| Page | Functionality |
|------|--------------|
| TodoList | Display list + pagination + status filter |
| TodoForm (create/edit) | Form submission, integrate Create/Update API |

### 1.4 Layout & Routing (`layout/` + `router/`)

- Main layout: top navbar + sidebar + content area
- Routes: `/` → list page, `/create` → create page, `/edit/:id` → edit page

**Deliverable:** Open the browser, see the Todo list, and be able to create, edit, and delete items. Full stack connected end to end.

---

## Phase 2: Solid Frontend Engineering

> Goal: Write production-grade frontend code — not just "it works."

### 2.1 State Management

Integrate TanStack Query (React Query) for server state:

```
Learn:
├── useQuery      — fetch data + automatic caching
├── useMutation   — submit data + auto-refresh list
├── Optimistic update — update UI before server responds
└── Cache invalidation — when to refetch
```

### 2.2 Component Architecture

```
TodoListPage (page component, handles data fetching)
├── TodoFilters (filter bar: status/priority/search)
├── TodoTable (table display)
│   ├── TodoStatusBadge (status badge)
│   └── TodoPriorityTag (priority tag)
├── TodoFormDialog (create/edit dialog)
└── Pagination (pagination controls)
```

### 2.3 UX Polish

| Feature | Learning Point |
|---------|---------------|
| Loading states | Skeleton / Spinner |
| Empty states | Guidance prompts when list is empty |
| Confirm dialogs | Confirm before delete |
| Form validation | Client-side validation + server error display |
| Toast notifications | Success/failure feedback |

**Deliverable:** Clean component structure, complete interactions, proper loading/empty/error handling.

---

## Phase 3: Deepen the Backend

> Goal: Evolve from "working API" to "near-production API."

### 3.1 User System

```
Add:
├── User model (users table)
├── JWT authentication middleware
├── Register / Login endpoints
├── Todo ↔ User association (each user sees only their own todos)
└── Password hashing (bcrypt)
```

This is one of the most critical backend skills. Completing auth will significantly level up your understanding of web backends.

### 3.2 API Enhancements

| Feature | Description |
|---------|-------------|
| Search | Fuzzy search by title |
| Sorting | Sort by priority / due date / created time |
| Batch operations | Bulk delete, bulk status update |
| Statistics | Return counts grouped by status/priority |

### 3.3 Input Validation & Security

```
Learn to handle:
├── SQL injection (understand how GORM parameterized queries prevent this)
├── XSS (risk of returning HTML from backend)
├── Rate limiting (prevent API abuse)
├── Thorough parameter validation (boundary values, type checks)
└── Unified error code system
```

**Deliverable:** Backend with user authentication, richer API endpoints, improved security.

---

## Phase 4: Full Stack Integration

> Goal: Frontend and backend truly work together, close to a real product experience.

### 4.1 Frontend Auth Integration

```
├── Login / Register pages
├── Token storage (localStorage)
├── Request interceptor to attach Token
├── Auto-redirect to login on 401
├── Route guards (require login to access)
└── User info display (avatar, username)
```

### 4.2 Responsive Design & Theming

```
├── MUI theme customization (brand colors, fonts)
├── Dark mode support
└── Mobile responsive layout
```

### 4.3 Production Deployment

```
├── Frontend build + Nginx configuration
├── Backend Docker deployment
├── Reverse proxy for frontend-backend communication
└── (Optional) Custom domain + HTTPS certificate
```

**Deliverable:** A complete app with auth, permissions, full-stack integration, accessible online.

---

## Phase 5: Advanced Challenges (Optional)

> Goal: Tackle deeper technical topics to stand out.

| Direction | Details | What You'll Learn |
|-----------|---------|-------------------|
| **Real-time notifications** | WebSocket push for due date reminders | Real-time communication |
| **Data visualization** | Dashboard with charts | ECharts / Recharts |
| **Offline support** | Service Worker + IndexedDB | PWA principles |
| **Internationalization** | i18n multi-language switching | i18n architecture |
| **Error monitoring** | Integrate Sentry | Production observability |
| **Unit testing** | Vitest + Testing Library | Frontend testing methods |
| **E2E testing** | Playwright end-to-end tests | Automated testing |

---

## Recommended Pace

```
Phase 1 → Phase 2 → Phase 3 → Phase 4 → Phase 5
 Connect    Solid     Backend    Full Stack   Advanced
   ↑          ↑         ↑          ↑           ↑
Required   Required  Suggested  Suggested    Optional
```

Each phase is an independent milestone. Complete one before moving to the next. Don't skip ahead — build a solid foundation before pursuing advanced features.
