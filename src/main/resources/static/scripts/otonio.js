const HOtoño = document.querySelector("#container-otoño");

const crearHojasOtoño = () => {

    let hojasOtoño = document.createElement('otoño');

    let x = innerWidth * Math.random();
    let size = (Math.random() * 30) + 2;
    let z = Math.random(Math.random()) * 100;
    let delay = Math.random() * 5;
    let duration = (Math.random() * 10) + 5;

    hojasOtoño.style.left = x + 'px'
    hojasOtoño.style.width = size + 'px'
    hojasOtoño.style.height = size + 'px'
    hojasOtoño.style.zIndex = z
    hojasOtoño.style.animationDelay = delay + 's'
    hojasOtoño.style.animationDuration = duration + 's'

    HOtoño.appendChild(hojasOtoño);

    /* para que no se sature */
    setTimeout(() => {
        hojasOtoño.remove();
    }, duration * 1000);

}

setInterval(crearHojasOtoño, 100);
