;
const getProducts = () => {
    return fetch("/api/products")
       .then((response) => response.json())
       .catch((error) => console.log(error))
}

const createHtmlElFromString = (template) => {
    let parent = document.createElement("div");
    parent.innerHTML = template.trim();

    return parent.firstChild;
}

const createProductComponent = (product) => {
    const template = `
        <li class="product">
            <span class="product__description">${product.description}</span>
            <div class="product__image-container">
                <img class="product__image" src="${product.picture}"/>
            </div>
            <span class="product__price">${product.price}</span>
            <button
                class="product__add-to-basket"
                data-product-id="${product.id}"
            >
                Add to basket
            </button>
        </li>
    `;

    return createHtmlElFromString(template);
}

const initializeAddToBasketHandler = (el) => {
    el.addEventListener('click', (e) => {
        let button = e.target;
        const productId = button.getAttribute('data-product-id');

        handleAddToBasket(productId)
            .then(() => refreshCurrentOffer())
            .catch((error) => console.log(error))
            ;
    });

    return el;
}

const handleAddToBasket = (productId) => {
    return fetch(`/api/basket/add/${productId}`, {
      method: 'POST'
    });
};

const refreshCurrentOffer = () => {
    return fetch("api/current-offer")
           .then((response) => response.json())
           .then(data => console.log(data))
           .catch((error) => console.log(error))
}

(() => {
    const productsList = document.querySelector('#products');
    getProducts()
        .then((products) => {
            products
                .map(p => createProductComponent(p))
                .map(productEl => initializeAddToBasketHandler(productEl))
                .forEach(productEl => {
                    productsList.appendChild(productEl)
                })
        })
})();