import { http, HttpResponse } from "msw";
import { BASE_URL } from "../constand";

const baseHandlers = [
  http.get(BASE_URL, async ({ request }) => {
    const urlInstance = new URL(request.url);
    const params = {};

    for (const [key, value] of urlInstance.searchParams.entries()) {
      params[key] = +value;
    }

    return HttpResponse.json({
      status: 200,
      params,
      data: [
        {
          userId: 1,
          id: 1,
          title: "first post title",
          body: "first post body",
        },
      ],
    });
  }),
  http.post(BASE_URL, () => {
    return HttpResponse.json({
      status: 200,
      data: [
        {
          userId: 1,
          id: 1,
          title: "first post title",
          body: "first post body",
        },
      ],
    });
  }),
  http.patch(BASE_URL, () => {
    return HttpResponse.json({
      status: 200,
      data: [
        {
          userId: 1,
          id: 1,
          title: "first post title",
          body: "first post body",
        },
      ],
    });
  }),
  http.put(BASE_URL, () => {
    return HttpResponse.json({
      status: 200,
      data: [
        {
          userId: 1,
          id: 1,
          title: "first post title",
          body: "first post body",
        },
      ],
    });
  }),
  http.delete(BASE_URL, () => {
    return HttpResponse.json({
      status: 200,
      data: [
        {
          userId: 1,
          id: 1,
          title: "first post title",
          body: "first post body",
        },
      ],
    });
  }),
  http.options(BASE_URL, () => {
    return HttpResponse.json({
      status: 200,
      data: [
        {
          userId: 1,
          id: 1,
          title: "first post title",
          body: "first post body",
        },
      ],
    });
  }),
  http.head(BASE_URL, () => {
    return HttpResponse.json({
      status: 200,
      data: [
        {
          userId: 1,
          id: 1,
          title: "first post title",
          body: "first post body",
        },
      ],
    });
  }),
];

export default baseHandlers;
