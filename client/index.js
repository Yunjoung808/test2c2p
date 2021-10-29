let drul1 = document.querySelector('.drul1');
let drul2 = document.querySelector('.drul2');

let drul1List = drul1.querySelectorAll('li');
let drul2List = drul2.querySelectorAll('li');

let drPlaceholder1 = document.getElementById('dropdownMenuButton1');
let drPlaceholder2 = document.getElementById('dropdownMenuButton2');

for (const i of drul1List) {
    let ctx = i.textContent;
    i.addEventListener('click', () => drPlaceholder1.textContent = ctx);
}

for (const i of drul2List) {
    let ctx = i.textContent;
    i.addEventListener('click', () => drPlaceholder2.textContent = ctx);
}

