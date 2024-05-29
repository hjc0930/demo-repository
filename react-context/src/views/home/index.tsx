import {
  Form,
  Field,
  FormElement,
  FieldWrapper,
} from "@progress/kendo-react-form";
import { Input } from "@progress/kendo-react-inputs";
import { Button } from "@progress/kendo-react-buttons";

const Home = () => {
  const handleSubmit = (values: any) => {
    console.log(values);
  };

  return (
    <Form
      onSubmit={handleSubmit}
      render={(formRenderProps) => (
        <FormElement
          style={{
            maxWidth: 650,
          }}
        >
          <FieldWrapper>
            <div className="k-form-field-wrap">
              <Field
                label={"First name"}
                name={"firstName"}
                component={Input}
                labelClassName={"k-form-label"}
              />
            </div>
          </FieldWrapper>
          <FieldWrapper>
            <div className="k-form-field-wrap">
              <Field
                name={"lastName"}
                component={Input}
                labelClassName={"k-form-label"}
                label={"Last name"}
              />
            </div>
          </FieldWrapper>
          <div className="k-form-buttons">
            <Button type={"submit"} disabled={!formRenderProps.allowSubmit}>
              Submit
            </Button>
          </div>
        </FormElement>
      )}
    />
  );
};

export default Home;
