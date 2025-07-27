const btn      = document.getElementById('subscribe-btn');
const modal    = document.getElementById('subscribe-modal');
const closeBtn = document.getElementById('modal-close');
const form     = document.getElementById('subscribe-form');

btn.addEventListener('click', () => {
  modal.classList.add('open');
});

closeBtn.addEventListener('click', () => {
  modal.classList.remove('open');
});

window.addEventListener('click', e => {
  if (e.target === modal) {
    modal.classList.remove('open');
  }
});

form.addEventListener('submit', e => {
  e.preventDefault();

  const gender = Array.from(
    form.querySelectorAll('input[name="gender"]:checked')
  ).map(x => x.value).join(',');

  const data = {
    gender,
    age:    Number(form.age.value),
    email:  form.email.value,
    timeSegment: form.timeSegment.value
  };

  fetch('/subscribes', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  })
  .then(res => {
    return res.json();
  })
  .then(json => {
    alert('구독이 완료되었습니다!');
  })
  .catch(err => {
    console.error(err);
    alert('구독 중 오류가 발생했습니다.');
  })
  .finally(() => {
    modal.classList.remove('open');
    form.reset();
  });
});