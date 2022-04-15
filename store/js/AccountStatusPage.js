import AccountLogInPage from '/js/AccountLogInPage.js'

export default function AccountStatusPage({ $app, $header }) {

    const $page = document.createElement('div')
    $page.className = 'header-right'
    $header.querySelector('.header-top').appendChild($page)

    this.setState = (nextState) => {
        this.state = nextState
        this.render()
    }

    this.render = () => {
        const logout = `<span class="header-logout">로그아웃</span>`
        const login = `<span class="header-login">로그인</span>`
        $page.innerHTML = sessionStorage.getItem("store-admin") ? logout : login
    }

    this.render()

    $page.addEventListener('click', (e) => {
        if (e.target.closest('.header-logout')) {
            sessionStorage.removeItem('store-admin')
            location.reload()
            return
        }

        if(e.target.closest('.header-login')){
            new AccountLogInPage({ $app, display: true })
        }
    })
}