package falsify.falsify.utils;

public class PID {
    private float sum = 0.0f;
    private float Kp;
    private float Ki;
    private float Kd;
    private float prev = 0.0f;

    public PID(float Kp, float Ki, float Kd) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
    }

    public float calcPID(float error) {
        sum = sum + error;
        if((prev > 0 && error < 0) || (prev < 0 && error > 0)) {
            sum /= 10;
        }
        float Ep = Kp * error;
        float Ei = Ki * sum;
        float Ed = Kd * (error-prev);
        prev = error;
        return Ep + Ei + Ed;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public float getKp() {
        return Kp;
    }

    public void setKp(float kp) {
        Kp = kp;
    }

    public float getKi() {
        return Ki;
    }

    public void setKi(float ki) {
        Ki = ki;
    }

    public float getKd() {
        return Kd;
    }

    public void setKd(float kd) {
        Kd = kd;
    }
}
