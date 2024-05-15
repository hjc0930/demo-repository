import pdfMake from "pdfmake/build/pdfmake";
import pdfFonts from "pdfmake/build/vfs_fonts";
import htmlToPdf from "html-to-pdfmake";

pdfMake.vfs = pdfFonts.pdfMake.vfs;

function App() {
  function printPDF() {
    const options = htmlToPdf(``);
    pdfMake
      .createPdf({
        content: [
          {
            canvas: [
              {
                type: "c",
              },
            ],
          },
        ],
      })
      .download();
  }
  return (
    <div className="App">
      <button onClick={printPDF}>printPDF</button>
      <canvas
        data-zr-dom-id="zr_0"
        width="990"
        height="1090"
        style={{
          position: "absolute",
          left: 0,
          top: 0,
          width: 495,
          height: 545,
          userSelect: "none",
          WebkitTapHighlightColor: "rgba(0, 0, 0, 0)",
          padding: 0,
          margin: 0,
          borderWidth: 0,
        }}
      ></canvas>
    </div>
  );
}

export default App;
