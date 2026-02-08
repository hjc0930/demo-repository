import { http, HttpResponse } from "msw";
import { BASE_URL } from "../constand";

const formDataHandlers = [
  http.post(BASE_URL + "/form", async ({ request }) => {
    const formData = await request.formData();
    const data = Object.fromEntries(formData.entries());

    return new HttpResponse(JSON.stringify(data));
  }),

  http.post(BASE_URL + "/file", async ({ request }) => {
    const data = await request.formData();
    const file = data.get("file");

    if (!file) {
      return new HttpResponse("Missing document", { status: 400 });
    }

    if (!(file instanceof File)) {
      return new HttpResponse("Uploaded document is not a File", {
        status: 400,
      });
    }

    return HttpResponse.json({
      contents: await file.text(),
    });
  }),
];

export default formDataHandlers;
