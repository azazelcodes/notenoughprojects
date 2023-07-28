from flask import Flask, redirect, render_template
import stripe

app = Flask(__name__)

@app.route('/')
def home():
    return render_template('home.html', current_page='home')

@app.route('/about')
def about():
    return render_template('about.html', current_page='about')

@app.route('/success')
def success():
    return render_template('success.html', current_page='buy')

stripe.api_key = 'sk_test_Y17KokhC3SRYCQTLYiU5ZCD2'
@app.route('/checkout')
def create_checkout_session():
    return redirect("success.html", code=303)
    try:
        checkout_session = stripe.checkout.Session.create(
            line_items=[
                {
                    # Provide the exact Price ID (for example, pr_1234) of the product you want to sell
                    'price': '{{PRICE_ID}}',
                    'quantity': 1,
                },
            ],
            mode='payment',
            success_url= 'success',
            cancel_url= 'cancle',
        )
    except Exception as e:
        return str(e)

    return redirect(checkout_session.url, code=303)

if __name__ == '__main__':
    app.run(port="6060")
