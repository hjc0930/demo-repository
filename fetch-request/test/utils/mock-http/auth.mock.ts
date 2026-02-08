import { delay, http, HttpResponse } from "msw";
import { BASE_URL } from "../constand";

const authUrl = BASE_URL + "/auth";

const authhandles = [
  http.post(authUrl, ({ request }) => {
    const token = request.headers.get("Token")?.split(" ")?.[1];

    if (!token) {
      return new HttpResponse("Unauthorized", {
        status: 401,
      });
    }

    if (token === "valid_token") {
      return new HttpResponse(JSON.stringify("OK"), {
        status: 200,
      });
    }
  }),
  http.get(authUrl + "/timeout", async () => {
    await delay(1500);
    return HttpResponse.json([
      {
        userId: 1,
        id: 1,
        title: "first post title",
        body: "first post body",
      },
    ]);
  }),
];

export default authhandles;
