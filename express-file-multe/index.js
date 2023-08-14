const express = require('express')
const multer = require('multer')
const cors = require('cors');

const app = express()
app.use(cors());

const upload = multer({ dest: 'uploads/' })

app.post('/upload', upload.single('file'), function (req, res, next) {
  console.log('req.file', req.file);
  console.log('req.body', req.body);
})

// 单字段上传
app.post('/uploads', upload.array('files', 2), function (req, res, next) {
  console.log('req.files', req.files);
  console.log('req.body', req.body);
}, function (err, req, res, next) {
  if (err instanceof multer.MulterError && err.code === 'LIMIT_UNEXPECTED_FILE') {
    res.status(400).end('Too many files uploaded');
  }
})

// 多字段上传
app.post('/multer-uploads', upload.fields([
  {
    name: "file1",
    maxCount: 2
  },
  {
    name: "file2",
    maxCount: 2
  }
]), function (req, res, next) {
  console.log('req.files', req.files);
  console.log('req.body', req.body);

}, function (err, _, res) {
  if (err instanceof multer.MulterError && err.code === 'LIMIT_UNEXPECTED_FILE') {
    res.status(400).end('Too many files uploaded');
  }
})

app.listen(3000, () => {
  console.log("http://localhost:3000")
});
