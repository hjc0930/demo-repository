/**
 * @type {HTMLInputElement}
 */
const fileInput = document.querySelector('#fileInput');

const chunkSize = 20 * 1024;

fileInput.onchange = async () => {
  const file = fileInput.files[0];
  const chunks = [];
  let startSize = 0;

  while (startSize < file.size) {
    chunks.push(file.slice(startSize, startSize + chunkSize));
    startSize += chunkSize;
  }

  const randomStr = Math.random().toString().slice(2, 8);
  const uploadCoection = chunks.map((chunk, key) => {
    const formData = new FormData();
    formData.set('key', randomStr + '_' + file.name + '-' + key);
    formData.append('files', chunk);
    return fetch('http://localhost:3000/upload', {
      method: 'post',
      body: formData,
    });
  });

  await Promise.all(uploadCoection);

  fetch('http://localhost:3000/merge?name=' + randomStr + '_' + file.name);
};
